import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { SupplierService } from '../services/supplier.service';
import { Supplier } from '../model/supplier.model';

@Component({
  selector: 'app-supplier-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './supplier-create.html'
})
export class SupplierCreate {
  
    supplier: Partial<Supplier> = {
      name: '',
      phone: '',
      email:''
    };
  
    constructor(
      private supplierService: SupplierService,
      private router: Router
    ) {}
  
    goBackToSuppliers(): void {
      this.router.navigate(['/suppliers']);
    }
  
    save(): void {
      this.supplierService.create(this.supplier).subscribe({
        next: () => {
          alert('Proveedor creado correctamente');
          this.router.navigate(['/suppliers/list']);
        },
        error: (err) => {
          console.error(err);
          alert('Error al crear el proveedor');
        }
      });
    }
  
    cancel(): void {
      this.router.navigate(['/suppliers']);
    }

}
