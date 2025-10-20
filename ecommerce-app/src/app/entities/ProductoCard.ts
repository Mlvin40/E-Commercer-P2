export interface ProductoCard {
  id: number;
  nombre: string;
  imagenUrl?: string;
  precio: number;
  stock: number;
  categoria: string;
  vendedorNombre?: string;
  vendedorCorreo?: string;
  avgRating: number;    
  ratingsCount: number; 
}
