import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { CategoryService } from '../services/category.service';
import { Category } from '../model/category.model';

@Component({
  selector: 'app-category-edit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category-edit.html'
})
export class CategoryEdit implements OnInit {

  category!: Category;
  categoryId!: number;

  constructor(
    private route: ActivatedRoute,
    private categoryService: CategoryService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.categoryId = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.categoryId) {
      alert('ID inválido');
      this.router.navigate(['/categories/list']);
      return;
    }

    this.loadCategory();
  }

  private loadCategory(): void {
    this.categoryService.getById(this.categoryId).subscribe({
      next: (data) => {
        this.category = data;
        this.cdr.detectChanges(); // 🔑 MISMO FIX QUE PRODUCT EDIT
      },
      error: () => {
        alert('No se pudo cargar la categoría');
        this.router.navigate(['/categories/list']);
      }
    });
  }

  save(): void {
    this.categoryService.update(this.categoryId, this.category).subscribe({
      next: () => {
        alert('Categoría actualizada');
        this.router.navigate(['/categories/list']);
      },
      error: () => alert('Error al actualizar')
    });
  }

  cancel(): void {
    this.router.navigate(['/categories/list']);
  }

  goBackToCategories(): void {
    this.router.navigate(['/categories']);
  }
}
