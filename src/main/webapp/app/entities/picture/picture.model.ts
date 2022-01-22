import { IMenuItem } from 'app/entities/menu-item/menu-item.model';

export interface IPicture {
  id?: number;
  name?: string | null;
  pictureUrl?: string | null;
  menuItem?: IMenuItem | null;
}

export class Picture implements IPicture {
  constructor(public id?: number, public name?: string | null, public pictureUrl?: string | null, public menuItem?: IMenuItem | null) {}
}

export function getPictureIdentifier(picture: IPicture): number | undefined {
  return picture.id;
}
