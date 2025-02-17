package com.sutonglabs.tracestore.ui.add_product_screen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.unit.dp
import com.sutonglabs.tracestore.models.ProductCreate

@OptIn(ExperimentalMaterial3Api::class)
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
    var expanded by remember { mutableStateOf(false) }

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

    LaunchedEffect(addProductStatus) {
        if (addProductStatus != null) {
            val message = if (addProductStatus) "Product added successfully!" else "Failed to add product"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            label = { Text("Product Description") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Product Price") },
            modifier = Modifier.fillMaxWidth()
        )

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "Select Category",
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Image")
        }

        Text(
            text = selectedImageUri?.lastPathSegment ?: "No image selected",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (selectedImageUri != null && selectedCategory != null) {
                    val imageFile = File(getFilePathFromUri(selectedImageUri!!, context))
                    val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

                    Log.d("AddProductScreen", "Uploading image: ${imageFile.absolutePath}")

                    addProductViewModel.uploadImage(imagePart)?.let { imageResponse ->
                        val imageUrl = imageResponse.path
                        val product = ProductCreate(
                            name = productName.text,
                            description = productDescription.text,
                            price = productPrice.text.toIntOrNull() ?: 0,
                            image = imageUrl,
                            categoryIds = listOf(selectedCategory!!.id)
                        )

                        Log.d("AddProductScreen", "Product payload: $product")
                        addProductViewModel.addProduct(product, context)
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

fun getFilePathFromUri(uri: Uri, context: Context): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val columnIndex = it.getColumnIndex(android.provider.MediaStore.Images.Media.DATA)
        if (it.moveToFirst()) {
            return it.getString(columnIndex)
        }
    }
    return null
}
