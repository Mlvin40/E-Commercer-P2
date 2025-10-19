export interface ProductoUpsert {
  nombre: string;
  descripcion?: string;
  imagenUrl?: string;
  precio: number;
  stock: number;
  estado: 'NUEVO'|'USADO';
  categoria: 'TECNOLOGIA'|'HOGAR'|'ACADEMICO'|'PERSONAL'|'DECORACION'|'OTRO';
}
