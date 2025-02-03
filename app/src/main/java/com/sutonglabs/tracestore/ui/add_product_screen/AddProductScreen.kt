package com.sutonglabs.tracestore.ui.add_product_screen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sutonglabs.tracestore.viewmodels.AddProductViewModel
import com.sutonglabs.tracestore.viewmodels.AddProductViewModelFactory
import com.sutonglabs.tracestore.repository.ProductRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File
import com.sutonglabs.tracestore.models.Category
import okhttp3.RequestBody.Companion.asRequestBody
import androidx.compose.ui.unit.dp  // Import for dp
import androidx.compose.material3.MaterialTheme // Import for typography
import com.sutonglabs.tracestore.models.ProductCreate

@Composable
fun AddProductScreen(
    navHostController: NavHostController,
    productRepository: ProductRepository,
    addProductViewModel: AddProductViewModel = viewModel(factory = AddProductViewModelFactory(productRepository))
) {
    var productName by remember { mutableStateOf(TextFieldValue()) }
    var productDescription by remember { mutableStateOf(TextFieldValue()) }
    var productPrice by remember { mutableStateOf(TextFieldValue()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // List of categories
    val categories = listOf(
        Category(1, "Food Crops"),
        Category(2, "Clothing"),
        Category(3, "Books"),
        Category(4, "Furniture")
    )

    val addProductStatus = addProductViewModel.addProductStatus.value
    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> selectedImageUri = uri }
    )

    // Handle add product status and show a toast
    LaunchedEffect(addProductStatus) {
        if (addProductStatus != null) {
            if (addProductStatus) {
                Toast.makeText(context, "Product added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // State to control dropdown visibility
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Product Name Field
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Product Description Field
        TextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            label = { Text("Product Description") },
            modifier = Modifier.fillMaxWidth()
        )

        // Product Price Field
        TextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Product Price") },
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown for selecting category
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = selectedCategory?.name ?: "Select Category",
                onValueChange = {},
                label = { Text("Category") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded } // Toggle dropdown visibility when clicked
            )

            // Dropdown menu for categories
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f) // Ensure dropdown is above other UI elements
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategory = category
                            expanded = false // Close dropdown after selection
                        }
                    )
                }
            }
        }

        // Button for selecting the image
        Button(
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Image")
        }

        // Display image selection status
        if (selectedImageUri != null) {
            Text(
                text = "Image Selected: ${selectedImageUri?.lastPathSegment}",
                style = MaterialTheme.typography.bodyLarge, // Corrected typography style
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "No image selected",
                style = MaterialTheme.typography.bodyLarge, // Corrected typography style
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Button for submitting the form
        Button(
            onClick = {
                if (selectedImageUri != null && selectedCategory != null) {
                    val imageFile = File(getFilePathFromUri(selectedImageUri!!, context)) // Convert URI to File
                    val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

                    // Log the image file path
                    Log.d("AddProductScreen", "Uploading image: ${imageFile.absolutePath}")

                    // Upload the image first
                    addProductViewModel.uploadImage(imagePart)?.let { imageResponse ->
                        // Assuming the response contains a URL or file path for the image
                        val imageUrl = imageResponse.path // Adjust according to your actual response

                        // Create the product object with the image URL
                        val product =  ProductCreate(
                            name = productName.text,
                            description = productDescription.text,
                            price = productPrice.text.toIntOrNull() ?: 0,
                            image = imageUrl, // Add the image URL to the product
                            categoryIds = listOf(selectedCategory!!.id) // Wrap in a list for category IDs
                        )

                        // Log the full product payload
                        Log.d("AddProductScreen", "Product payload with image: $product")

                        // Call the method to upload the product and image
                        addProductViewModel.addProduct(product,context)
                    } ?: run {
                        Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please select an image and a category", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Product")
        }
    }
}


// Function to get file path from URI
fun getFilePathFromUri(uri: Uri, context: Context): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.let {
        val columnIndex = it.getColumnIndex(android.provider.MediaStore.Images.Media.DATA)
        if (it.moveToFirst()) {
            return it.getString(columnIndex) // Returns the absolute file path
        }
    }
    return null
}
