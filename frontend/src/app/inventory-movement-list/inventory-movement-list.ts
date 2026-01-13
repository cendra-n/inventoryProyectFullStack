import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { InventoryService } from '../services/inventoryMovement.service';
import { InventoryMovement } from '../model/inventory-movement.model';

@Component({
  selector: 'app-inventory-movement-list',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './inventory-movement-list.html'
})
export class InventoryMovementList implements OnInit {

  movements: InventoryMovement[] = [];
  loading = false;

  constructor(
    private inventoryService: InventoryService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadMovements();
  }

  goToBackInventories(): void {
    this.router.navigate(['/inventories']);
  }

  private loadMovements(): void {
    this.loading = true;

    this.inventoryService.getAll().subscribe({
      next: (data) => {
        this.movements = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar movimientos', err);
        alert('No se pudieron cargar los movimientos');
        this.loading = false;
      }
    });
  }
}

