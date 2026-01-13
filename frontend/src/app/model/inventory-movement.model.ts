import { Product } from './product.model';

export enum MovementType {
  IN = 'IN',
  OUT = 'OUT'
}

export class InventoryMovement {
  id!: number;
  quantity!: number;
  type!: 'IN' | 'OUT';
  date!: Date;
  productId!: Product;
  productName!: string;

}

