import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { ProductService } from '../services/product.service';
import { CategoryService } from '../services/category.service';
import { SupplierService } from '../services/supplier.service';

import { Product } from '../model/product.model';
import { Category } from '../model/category.model';
import { Supplier } from '../model/supplier.model';

@Component({
  selector: 'app-product-edit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-edit.html'
})
export class ProductEdit implements OnInit {

  product!: Product;
  productId!: number;

  categories: Category[] = [];
  suppliers: Supplier[] = [];

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private categoryService: CategoryService,
    private supplierService: SupplierService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));

    this.loadProduct();
    this.loadCategories();
    this.loadSuppliers();
  }

  goBackToProducts(): void {
    this.router.navigate(['/products']);
  }


  private loadProduct(): void {
    this.productService.getById(this.productId).subscribe({
      next: (data) => {
        this.product = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar producto', err);
        alert('No se pudo cargar el producto');
        this.router.navigate(['/products/list']);
      }
    });
  }

  private loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (data) => this.categories = data,
      error: (err) => console.error('Error al cargar categorías', err)
    });
  }

  private loadSuppliers(): void {
    this.supplierService.getAll().subscribe({
      next: (data) => this.suppliers = data,
      error: (err) => console.error('Error al cargar proveedores', err)
    });
  }

  save(): void {
    this.productService.update(this.productId, this.product).subscribe({
      next: () => {
        alert('Producto actualizado correctamente');
        this.router.navigate(['/products/list']);
      },
      error: (err) => {
        console.error('Error al actualizar producto', err);
        alert('Error al actualizar el producto');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/products/list']);
  }
}
