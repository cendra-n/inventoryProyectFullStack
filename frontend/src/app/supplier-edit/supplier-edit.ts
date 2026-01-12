import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SupplierService } from '../services/supplier.service';
import { Supplier } from '../model/supplier.model';

@Component({
  selector: 'app-supplier-edit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './supplier-edit.html' 
})
export class SupplierEdit {
  
    supplier!: Supplier;
    supplierId!: number;
  
    constructor(
      private route: ActivatedRoute,
      private supplierService: SupplierService,
      private router: Router,
      private cdr: ChangeDetectorRef
    ) {}
  
    ngOnInit(): void {
      this.supplierId = Number(this.route.snapshot.paramMap.get('id'));
  
      if (!this.supplierId) {
        alert('ID inválido');
        this.router.navigate(['/supplier/list']);
        return;
      }
  
      this.loadSupplier();
    }
  
    private loadSupplier(): void {
      this.supplierService.getById(this.supplierId).subscribe({
        next: (data) => {
          this.supplier = data;
          this.cdr.detectChanges(); 
        },
        error: () => {
          alert('No se pudo cargar el proveedor');
          this.router.navigate(['/suppliers/list']);
        }
      });
    }
  
    save(): void {
      this.supplierService.update(this.supplierId, this.supplier).subscribe({
        next: () => {
          alert('Proveedor actualizado');
          this.router.navigate(['/suppliers/list']);
        },
        error: () => alert('Error al actualizar')
      });
    }
  
    cancel(): void {
      this.router.navigate(['/supplier/list']);
    }
  
    goBackToSuppliers(): void {
      this.router.navigate(['/suppliers']);
    }

}
