jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { RestaurantOrderItemService } from '../service/restaurant-order-item.service';

import { RestaurantOrderItemDeleteDialogComponent } from './restaurant-order-item-delete-dialog.component';

describe('RestaurantOrderItem Management Delete Component', () => {
  let comp: RestaurantOrderItemDeleteDialogComponent;
  let fixture: ComponentFixture<RestaurantOrderItemDeleteDialogComponent>;
  let service: RestaurantOrderItemService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RestaurantOrderItemDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(RestaurantOrderItemDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RestaurantOrderItemDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RestaurantOrderItemService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
