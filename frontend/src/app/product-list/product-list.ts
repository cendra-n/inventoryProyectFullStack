import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-list.html'
})
export class ProductList {

  products = [
    {
      id: 1,
      name: 'Laptop',
      description: 'Laptop Lenovo ThinkPad',
      price: 1200,
      stock: 10
    },
    {
      id: 2,
      name: 'Mouse',
      description: 'Mouse inalámbrico',
      price: 25,
      stock: 50
    }
  ];

  constructor(private router: Router) {}

  goBackToProducts() {
    this.router.navigate(['/products']);
  }

  editProduct(id: number) {
    console.log('Editar producto con id:', id);
    // más adelante: this.router.navigate(['/products/edit', id]);
  }

  deleteProduct(id: number) {
    const confirmed = confirm('¿Seguro que deseas eliminar este producto?');
    if (confirmed) {
      console.log('Eliminar producto con id:', id);
    }
  }
}
