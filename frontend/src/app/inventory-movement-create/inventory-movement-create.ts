import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { InventoryService } from '../services/inventoryMovement.service';
import { ProductService } from '../services/product.service';

import { Product } from '../model/product.model';

@Component({
  selector: 'app-inventory-movement-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventory-movement-create.html'
})
export class InventoryMovementCreate implements OnInit {

  products: Product[] = [];

  movement = {
    productId: null as number | null,
    type: '',
    quantity: null as number | null
  };

  loading = false;

  constructor(
    private inventoryService: InventoryService,
    private productService: ProductService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  private loadProducts(): void {
    this.productService.getAll().subscribe({
      next: data => this.products = data,
      error: err => {
        console.error(err);
        alert('No se pudieron cargar los productos');
      }
    });
  }

  /*
  save(): void {
    if (!this.movement.productId || !this.movement.type || !this.movement.quantity) {
      alert('Todos los campos son obligatorios');
      return;
    }

    this.loading = true;

    this.inventoryService.create(this.movement).subscribe({
      next: () => {
        alert('Movimiento registrado correctamente');
        this.router.navigate(['/inventories/list']);
      },
      error: err => {
        console.error(err);
        alert(err?.error?.message || 'Error al registrar el movimiento');
        this.loading = false;
      }
    });
  }
    */

  cancel(): void {
    this.router.navigate(['/inventories']);
  }
}


