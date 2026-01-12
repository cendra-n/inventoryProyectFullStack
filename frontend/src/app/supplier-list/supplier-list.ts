import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { SupplierService } from '../services/supplier.service';
import { Supplier } from '../model/supplier.model';

@Component({
  selector: 'app-supplier-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './supplier-list.html'
})
export class SupplierList {
  suppliers: Supplier[] = [];
    allSuppliers: Supplier[] = [];
    searchText: string = '';
    loading: boolean = false;

    constructor(
    private supplierService: SupplierService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadSuppliers();
  }

  private loadSuppliers(): void {
  this.loading = true;

  this.supplierService.getAll().subscribe({
      next: (data) => {
        this.suppliers = data;
        this.allSuppliers = data;
        this.loading = false;
        this.cdr.detectChanges(); // 🔥 CLAVE PARA QUE SE RENDERICE
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  goBackToSuppliers(): void {
    this.router.navigate(['/suppliers']);
  }

    
  search(): void {
    if (!this.searchText || this.searchText.trim() === '') {
      this.loadSuppliers();
      return;
    }

    this.supplierService.searchByName(this.searchText.trim())
      .subscribe({
        next: (data) => {
          this.suppliers = data;   
        },
        error: () => {
          this.suppliers = [];
          alert('No se encontraron proveedores');
        }
      });
  }


  clearSearch(): void {
    this.searchText = '';
    this.loadSuppliers();
  }

  editSupplier(id: number): void {
    this.router.navigate(['/suppliers/edit', id]);
  }

  deleteSupplier(id: number): void {
  if (!confirm('¿Seguro que deseas eliminar este proveedor?')) return;

  this.supplierService.delete(id).subscribe({
    next: () => {
      alert('Proveedor eliminado correctamente');
      this.loadSuppliers();
    },
    error: (err) => {
      alert('No se puede eliminar el proveedor porque tiene productos asociados');
    }
  });
}








}

