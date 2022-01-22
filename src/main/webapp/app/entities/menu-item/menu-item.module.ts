import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MenuItemComponent } from './list/menu-item.component';
import { MenuItemDetailComponent } from './detail/menu-item-detail.component';
import { MenuItemUpdateComponent } from './update/menu-item-update.component';
import { MenuItemDeleteDialogComponent } from './delete/menu-item-delete-dialog.component';
import { MenuItemRoutingModule } from './route/menu-item-routing.module';

@NgModule({
  imports: [SharedModule, MenuItemRoutingModule],
  declarations: [MenuItemComponent, MenuItemDetailComponent, MenuItemUpdateComponent, MenuItemDeleteDialogComponent],
  entryComponents: [MenuItemDeleteDialogComponent],
})
export class MenuItemModule {}
