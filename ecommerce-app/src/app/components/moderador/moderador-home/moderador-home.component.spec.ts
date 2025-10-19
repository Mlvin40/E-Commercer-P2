import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModeradorHomeComponent } from './moderador-home.component';

describe('ModeradorHomeComponent', () => {
  let component: ModeradorHomeComponent;
  let fixture: ComponentFixture<ModeradorHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModeradorHomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModeradorHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
