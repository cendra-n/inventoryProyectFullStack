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

  searchText: string = '';


  constructor(
    private productService: ProductService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  private loadProducts(): void {
    this.productService.getAll().subscribe({
      next: (data) => {
        this.products = data;
        this.cdr.detectChanges(); // 🔥 CLAVE PARA QUE SE RENDERICE
      },
      error: (error) => {
        console.error('Error al cargar productos', error);
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
    if (!this.searchText.trim()) {
      return;
    }

    this.productService.searchByName(this.searchText).subscribe({
      next: (product) => {
        // lo convertimos en array para reutilizar la tabla
        this.products = [product];
      },
      error: (err) => {
        console.error('Error en búsqueda', err);
        alert('Producto no encontrado');
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
