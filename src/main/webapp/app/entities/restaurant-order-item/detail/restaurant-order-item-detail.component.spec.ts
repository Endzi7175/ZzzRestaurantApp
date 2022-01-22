import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RestaurantOrderItemDetailComponent } from './restaurant-order-item-detail.component';

describe('RestaurantOrderItem Management Detail Component', () => {
  let comp: RestaurantOrderItemDetailComponent;
  let fixture: ComponentFixture<RestaurantOrderItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestaurantOrderItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ restaurantOrderItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RestaurantOrderItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RestaurantOrderItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load restaurantOrderItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.restaurantOrderItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
