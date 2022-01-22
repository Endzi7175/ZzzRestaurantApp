import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMenuItem } from '../menu-item.model';
import { MenuItemService } from '../service/menu-item.service';

@Component({
  templateUrl: './menu-item-delete-dialog.component.html',
})
export class MenuItemDeleteDialogComponent {
  menuItem?: IMenuItem;

  constructor(protected menuItemService: MenuItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.menuItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
