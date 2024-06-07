import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SignUpCredentials } from '../../nav-bar/signup/signupcredentials';
import { map } from 'rxjs';
import { APIResponse } from '../model/apiResponse';

const AUTH_API = 'http://localhost:8383';
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  observe: ''
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  register(signUpCredentials: object) {
    return this.http.post<APIResponse>(
      AUTH_API + '/register',
      signUpCredentials,
      {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      }
    );
  }

  login(signUpCredentials: Object) {
    return this.http.post<APIResponse>(
      AUTH_API + '/login',
      signUpCredentials,
      {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      }
    )
  }

  verify(email: string) {
    return this.http.get<APIResponse>(
      AUTH_API + '/verify-email',
      {
        headers: new HttpHeaders({'Content-Type': 'application/json'}),
        params: new HttpParams().set('email', email)
      }
    );
  }

  logoff(email: string) {
    const httpHeader = new HttpHeaders();
    httpHeader.append('Content-Type', 'application/json');
    httpHeader.append('authorization', 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6InVzZTFAeW9wbWFpbC5jb20iLCJyb2xlIjoiVVNFUiIsInN1YiI6InVzZTFAeW9wbWFpbC5jb20iLCJpYXQiOjE3MTc3NDk1MjUsImV4cCI6MTcxNzgzNTkyNX0.Asg_YcRN4IQjwW9xnP8x7GSrnsoA3Fv9DomanhbzJ28iABCm1sZfv7fei5RCcD7kGjdcl4n2dMHErt0GzdBaOQ');
    return this.http.post<APIResponse>(
      AUTH_API + '/logoff',
      {
        httpHeader,
        params: new HttpParams().set('email', email)
      }
    )
  }
}
