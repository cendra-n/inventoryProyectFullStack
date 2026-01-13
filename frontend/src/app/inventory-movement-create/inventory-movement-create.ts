import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import {
  InventoryService,
  InventoryMovementCreateRequest
} from '../services/inventoryMovement.service';

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
  loading = false;

  movement: InventoryMovementCreateRequest = {
    productId: 0,
    quantity: 1,
    type: 'IN'
  };

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
      error: () => alert('No se pudieron cargar los productos')
    });
  }

  save(): void {
    if (
      this.movement.productId <= 0 ||
      this.movement.quantity <= 0
    ) {
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
        alert(err?.error?.message || 'Error al registrar el movimiento');
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/inventories']);
  }
}
