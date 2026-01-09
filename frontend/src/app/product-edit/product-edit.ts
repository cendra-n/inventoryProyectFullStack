import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { ProductService } from '../services/product.service';
import { Product } from '../model/product.model';

@Component({
  selector: 'app-product-edit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-edit.html'
})
export class ProductEdit implements OnInit {

  product!: Product;
  productId!: number;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProduct();
  }

  loadProduct(): void {
    this.productService.getById(this.productId).subscribe({
      next: (data) => {
        this.product = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error al cargar producto', err)
    });
  }

  save(): void {
    this.productService.update(this.productId, this.product).subscribe({
      next: () => {
        alert('Producto actualizado correctamente');
        this.router.navigate(['/products/list']);
      },
      error: (err) => console.error('Error al actualizar', err)
    });
  }

  cancel(): void {
    this.router.navigate(['/products/list']);
  }
}
