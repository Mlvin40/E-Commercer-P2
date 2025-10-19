import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModeracionListaComponent } from './moderacion-lista.component';

describe('ModeracionListaComponent', () => {
  let component: ModeracionListaComponent;
  let fixture: ComponentFixture<ModeracionListaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModeracionListaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModeracionListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
