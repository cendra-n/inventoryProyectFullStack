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

  editProduct(id: number): void {
    this.router.navigate(['/products/edit', id]);
  }
 
   
  search(): void {
    const text = this.searchText.trim();

    if (!text) {
      this.products = this.allProducts;
      return;
    }

    this.productService.searchByName(text).subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (err) => {
        console.error('Error en búsqueda', err);
        this.products = [];
      }
    });
  }

  clearSearch(): void {
    this.searchText = '';
    this.loadProducts(); // vuelve a cargar todos
  }


  deleteProduct(id: number): void {
    const confirmed = confirm('¿Seguro que deseas eliminar este producto?');

    if (!confirmed) return;

    this.productService.delete(id).subscribe({
      next: () => {
        alert('Producto eliminado correctamente');
        this.loadProducts(); // 🔥 refresca la tabla
      },
      error: (err) => {
        console.error(err);
        alert('Error al eliminar el producto');
      }
    });
  }


}
