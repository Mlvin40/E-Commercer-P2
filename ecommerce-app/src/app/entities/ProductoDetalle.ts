export interface ProductoDetalle {
  id: number;
  nombre: string;
  descripcion: string;
  imagenUrl: string;
  precio: number;
  stock: number;
  estado: string;
  categoria: string;
  vendedorNombre: string;
  avgRating: number; 
  ratingsCount: number;
}