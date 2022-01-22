import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PictureService } from '../service/picture.service';
import { IPicture, Picture } from '../picture.model';

import { PictureUpdateComponent } from './picture-update.component';

describe('Picture Management Update Component', () => {
  let comp: PictureUpdateComponent;
  let fixture: ComponentFixture<PictureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pictureService: PictureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PictureUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PictureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PictureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pictureService = TestBed.inject(PictureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const picture: IPicture = { id: 456 };

      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(picture));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Picture>>();
      const picture = { id: 123 };
      jest.spyOn(pictureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: picture }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pictureService.update).toHaveBeenCalledWith(picture);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Picture>>();
      const picture = new Picture();
      jest.spyOn(pictureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: picture }));
      saveSubject.complete();

      // THEN
      expect(pictureService.create).toHaveBeenCalledWith(picture);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Picture>>();
      const picture = { id: 123 };
      jest.spyOn(pictureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pictureService.update).toHaveBeenCalledWith(picture);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
