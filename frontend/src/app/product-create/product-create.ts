import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { ProductService } from '../services/product.service';
import { CategoryService } from '../services/category.service';
import { SupplierService } from '../services/supplier.service';

import { Product } from '../model/product.model';
import { Category } from '../model/category.model';
import { Supplier } from '../model/supplier.model';

@Component({
  selector: 'app-product-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-create.html'
})
export class ProductCreate implements OnInit {

  product: Partial<Product> = {
    name: '',
    description: '',
    price: 0,
    stock: 0,
    categoryId: 1,
    supplierId: 1
  };

  categories: Category[] = [];
  suppliers: Supplier[] = [];

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private supplierService: SupplierService,
    private router: Router
  ) {}

  goBackToProducts(): void {
    this.router.navigate(['/products']);
  }
  
  ngOnInit(): void {
    this.categoryService.getAll().subscribe(data => this.categories = data);
    this.supplierService.getAll().subscribe(data => this.suppliers = data);
  }

  save(): void {
    this.productService.create(this.product).subscribe({
      next: () => {
        alert('Producto creado correctamente');
        this.router.navigate(['/products/list']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al crear producto');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/products']);
  }
}
