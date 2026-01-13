import { Routes } from '@angular/router';
import { Home } from './home/home';
import { ProductHome } from './product-home/product-home';
import { ProductList } from './product-list/product-list';
import { ProductEdit } from './product-edit/product-edit';
import { ProductCreate } from './product-create/product-create';
import { CategoryHome } from './category-home/category-home';
import { CategoryList } from './category-list/category-list';
import { CategoryEdit } from './category-edit/category-edit';
import { CategoryCreate } from './category-create/category-create';
import { SupplierHome } from './supplier-home/supplier-home';
import { SupplierCreate } from './supplier-create/supplier-create';
import { SupplierEdit } from './supplier-edit/supplier-edit';
import { SupplierList } from './supplier-list/supplier-list';
import { InventoryMovementHome } from './inventory-movement-home/inventory-movement-home';
import { InventoryMovementCreate } from './inventory-movement-create/inventory-movement-create';
import { InventoryMovementList } from './inventory-movement-list/inventory-movement-list';

export const routes: Routes = [
  { path: '', component: Home },

  {
    path: 'products',
    component: ProductHome
  },

  {
    path: 'products/list',
    component: ProductList
  },
  
  { path: 'products/edit/:id',
     component: ProductEdit 
  },

  {
  path: 'products/create',
  component: ProductCreate
  },

  {
    path: 'categories',
    component: CategoryHome
  },

  { 
    path: 'categories/list',
    component: CategoryList
  },
  
  { 
   path: 'categories/edit/:id',
   component: CategoryEdit
  },

  { 
    path: 'categories/create',
    component: CategoryCreate
  },
  
  {
    path: 'suppliers',
    component: SupplierHome
  },

  {
    path: 'suppliers/list',
    component: SupplierList
  },

  {
    path: 'suppliers/edit/:id',
    component: SupplierEdit
  },

  {
    path: 'suppliers/create',
    component: SupplierCreate
  },

  {
    path: 'inventories',
    component: InventoryMovementHome
  },

  {
    path: 'inventories/create',
    component: InventoryMovementCreate
  },

  {
    path: 'inventories/list',
    component: InventoryMovementList
  },

  { path: '**', redirectTo: '' }

];
