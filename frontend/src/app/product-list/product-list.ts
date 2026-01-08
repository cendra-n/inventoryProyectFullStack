import { Component, OnInit } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';
import { Router } from '@angular/router';
import { ProductService, Product } from '../services/product.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    NgFor   // 👈 ESTO ES LO QUE FALTABA
  ],
  templateUrl: './product-list.html'
})
export class ProductList implements OnInit {

  products: Product[] = [];

  constructor(
    private productService: ProductService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getAll().subscribe({
      next: (data) => {
        console.log('DATA:', data);
        this.products = data;
      },
      error: (err) => {
        console.error('Error al cargar productos', err);
      }
    });
  }

  goBackToProducts() {
    this.router.navigate(['/products']);
  }

  editProduct(id: number) {
    console.log('Editar producto con id:', id);
  }

  deleteProduct(id: number) {
    console.log('Eliminar producto con id:', id);
  }
}
