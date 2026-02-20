package com.sutonglabs.tracestore.ui.add_product_screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sutonglabs.tracestore.models.Category
import com.sutonglabs.tracestore.models.ProductCreate
import com.sutonglabs.tracestore.repository.ProductRepository
import com.sutonglabs.tracestore.ui.image_preview_grid.ImagePreviewGrid
import com.sutonglabs.tracestore.viewmodels.AddProductViewModel
import com.sutonglabs.tracestore.viewmodels.AddProductViewModelFactory
import com.sutonglabs.tracestore.viewmodels.CategoryViewModel
import com.sutonglabs.tracestore.viewmodels.helper.ImageFileHelper
import com.sutonglabs.tracestore.viewmodels.state.AddProductState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddProductScreen(
    navHostController: NavHostController,
    productRepository: ProductRepository,
    addProductViewModel: AddProductViewModel = viewModel(
        factory = AddProductViewModelFactory(productRepository)
    )
) {

    var productName by remember { mutableStateOf(TextFieldValue("Realme 6")) }
    var productDescription by remember { mutableStateOf(TextFieldValue("Mobile Phone")) }
    var productPrice by remember { mutableStateOf(TextFieldValue("16999")) }

    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedCategories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    val categoryViewModel: CategoryViewModel = viewModel()
    val categories = categoryViewModel.categories

    val state = addProductViewModel.state.value
    val context = LocalContext.current

    // Image picker
    val pickImagesLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) { uris ->
            selectedImageUris = uris.take(6)
        }

    //  State handling
    LaunchedEffect(state) {
        when (state) {
            is AddProductState.Success -> {
                Toast.makeText(
                    context,
                    "Product added successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                navHostController.popBackStack()
            }

            is AddProductState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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

        //  Categories dropdown (multi-select)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value =
                    if (selectedCategories.isEmpty())
                        "Select Categories"
                    else
                        selectedCategories.joinToString { it.name },
                onValueChange = {},
                readOnly = true,
                label = { Text("Categories") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    val isSelected =
                        selectedCategories.any { it.id == category.id }

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(category.name)
                            }
                        },
                        onClick = {
                            selectedCategories =
                                if (isSelected)
                                    selectedCategories.filterNot { it.id == category.id }
                                else
                                    selectedCategories + category
                        }
                    )
                }
            }
        }

        // Selected category chips
        if (selectedCategories.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedCategories.forEach { category ->
                    AssistChip(
                        onClick = {
                            selectedCategories =
                                selectedCategories.filterNot { it.id == category.id }
                        },
                        label = { Text(category.name) }
                    )
                }
            }
        }

        // Image picker button
        Button(
            onClick = { pickImagesLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Images")
        }

        if (selectedImageUris.isEmpty()) {
            Text("No images selected")
        } else {
            ImagePreviewGrid(
                imageUris = selectedImageUris,
                onRemove = { uri ->
                    selectedImageUris =
                        selectedImageUris.filterNot { it == uri }
                }
            )
        }


        // Submit
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is AddProductState.Loading,
            onClick = {

                if (selectedImageUris.isEmpty() || selectedCategories.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Select images and categories",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                val product = ProductCreate(
                    name = productName.text,
                    description = productDescription.text,
                    price = productPrice.text.toIntOrNull() ?: 0,
                    categoryIds = selectedCategories.map { it.id },
                    image_uuids = emptyList()
                )

                addProductViewModel.createProduct(
                    context = context,
                    product = product,
                    imageUris = selectedImageUris
                )

            }
        ) {
            if (state is AddProductState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Add Product")
            }
        }
    }
}
