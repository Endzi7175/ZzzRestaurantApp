import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPicture, Picture } from '../picture.model';
import { PictureService } from '../service/picture.service';

@Component({
  selector: 'jhi-picture-update',
  templateUrl: './picture-update.component.html',
})
export class PictureUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    pictureUrl: [],
  });

  constructor(protected pictureService: PictureService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ picture }) => {
      this.updateForm(picture);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const picture = this.createFromForm();
    if (picture.id !== undefined) {
      this.subscribeToSaveResponse(this.pictureService.update(picture));
    } else {
      this.subscribeToSaveResponse(this.pictureService.create(picture));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPicture>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(picture: IPicture): void {
    this.editForm.patchValue({
      id: picture.id,
      name: picture.name,
      pictureUrl: picture.pictureUrl,
    });
  }

  protected createFromForm(): IPicture {
    return {
      ...new Picture(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      pictureUrl: this.editForm.get(['pictureUrl'])!.value,
    };
  }
}
