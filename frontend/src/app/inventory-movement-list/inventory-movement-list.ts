import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { InventoryService } from '../services/inventoryMovement.service';
import { InventoryMovement } from '../model/inventory-movement.model';
import { ProductService } from '../services/product.service';
import { Product } from '../model/product.model';

@Component({
  selector: 'app-inventory-movement-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventory-movement-list.html'
})
export class InventoryMovementList implements OnInit {

  movements: InventoryMovement[] = [];
  products: Product[] = [];

  // filtros
  productId: number | null = null;
  startDate: string = '';
  endDate: string = '';

  loading = false;

  constructor(
    private inventoryService: InventoryService,
    private productService: ProductService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadAllMovements();
  }

  goToBackInventories(): void {
    this.router.navigate(['/inventories']);
  }

  // -------------------------
  // CARGAS INICIALES
  // -------------------------
  private loadAllMovements(): void {
    this.loading = true;

    this.inventoryService.getAll().subscribe({
      next: data => {
        this.movements = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        console.error(err);
        alert('No se pudieron cargar los movimientos');
        this.loading = false;
      }
    });
  }

  private loadProducts(): void {
    this.productService.getAll().subscribe({
      next: data => this.products = data,
      error: () => alert('No se pudieron cargar los productos')
    });
  }

  // -------------------------
  // BÚSQUEDA INTELIGENTE
  // -------------------------
  search(): void {
    this.loading = true;

    // 1️⃣ Producto + Fechas
    if (this.productId && this.startDate && this.endDate) {
      this.inventoryService
        .getByProductAndDate(this.productId, this.startDate, this.endDate)
        .subscribe(this.handleResponse());
      return;
    }

    // 2️⃣ Solo Producto
    if (this.productId) {
      this.inventoryService
        .getByProduct(this.productId)
        .subscribe(this.handleResponse());
      return;
    }

    // 3️⃣ Solo Fechas
    if (this.startDate && this.endDate) {
      this.inventoryService
        .getByDateRange(this.startDate, this.endDate)
        .subscribe(this.handleResponse());
      return;
    }

    // 4️⃣ Sin filtros
    this.loadAllMovements();
  }

  clearFilters(): void {
    this.productId = null;
    this.startDate = '';
    this.endDate = '';
    this.loadAllMovements();
  }

  private handleResponse() {
    return {
      next: (data: InventoryMovement[]) => {
        this.movements = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error(err);
        alert('Error al realizar la búsqueda');
        this.loading = false;
      }
    };
  }
}
