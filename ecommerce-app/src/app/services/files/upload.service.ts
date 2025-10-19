import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UploadService {
  private base = environment.apiUrl; 

  constructor(private http: HttpClient) {}

  uploadImage(file: File): Observable<string> {
    const form = new FormData();
    form.append('file', file);

    return this.http.post<{ url: string }>(`${this.base}/files/upload`, form)
      .pipe(map(r => r.url));
  }
}
