import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { CategoryService } from '../services/category.service';
import { Category } from '../model/category.model';

@Component({
  selector: 'app-category-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category-create.html'
})
export class CategoryCreate {

  category: Partial<Category> = {
    name: '',
    description: ''
  };

  constructor(
    private categoryService: CategoryService,
    private router: Router
  ) {}

  goBackToCategories(): void {
    this.router.navigate(['/categories']);
  }

  save(): void {
    this.categoryService.create(this.category).subscribe({
      next: () => {
        alert('Categoría creada correctamente');
        this.router.navigate(['/categories/list']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al crear la categoría');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/categories']);
  }
}
