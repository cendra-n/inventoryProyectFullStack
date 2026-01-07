import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-product-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './product-home.html'
})
export class ProductHome {}
