import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { ProductService } from '../services/product.service';
import { Product } from '../model/product.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-list.html'
})
export class ProductList implements OnInit {

  products: Product[] = [];
  allProducts: Product[] = [];
  searchText: string = '';
  loading: boolean = false;



  constructor(
    private productService: ProductService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  private loadProducts(): void {
  this.loading = true;

  this.productService.getAll().subscribe({
      next: (data) => {
        this.products = data;
        this.allProducts = data;
        this.loading = false;
        this.cdr.detectChanges(); // 🔥 CLAVE PARA QUE SE RENDERICE
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  goBackToProducts(): void {
    this.router.navigate(['/products']);
  }

 
  search(): void {
    if (!this.searchText || this.searchText.trim() === '') {
      this.loadProducts();
      return;
  }

  this.productService.searchByName(this.searchText.trim())
    .subscribe({
      next: data => this.products = data,
      error: () => {
        this.products = [];
        alert('No se encontraron productos');
      }
    });
  }

  clearSearch(): void {
    this.searchText = '';
    this.loadProducts();
  }

  editProduct(id: number): void {
    this.router.navigate(['/products/edit', id]);
  }

  deleteProduct(id: number): void {
    const confirmed = confirm('¿Seguro que deseas eliminar este producto?');

    if (!confirmed) return;

    this.productService.delete(id).subscribe({
      next: () => {
        alert('Producto eliminado correctamente');
        this.loadProducts();
      },
      error: (err) => {
        console.error(err);

        // 👉 Si el backend devolvió 409
        if (err.status === 409) {
          alert(err.error.error); 
          // muestra: "No se puede eliminar un producto con movimientos..."
        } else {
          alert('Error al eliminar el producto');
        }
      }
    });
  }

  
  downloadPdf(): void {
    this.productService.downloadProductsPdf().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = 'ListaDeProductos.pdf';
        a.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al descargar PDF', err);
        alert('Error al descargar el PDF');
      }
    });
  }



}
