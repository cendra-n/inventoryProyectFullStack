export class Product {
    id!: number;
    name!: string;
    description!: string;
    price!: number;
    stock!: number;
    categoryId?: number;
    supplierId?:number;
}
