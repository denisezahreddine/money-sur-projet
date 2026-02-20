import {User} from './user.models';

export interface LoginResponse {
  jwt:string;
  user:User;

}
