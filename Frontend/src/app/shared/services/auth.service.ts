import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SignUpCredentials } from '../../nav-bar/signup/signupcredentials';
import { APIResponse } from '../model/apiResponse';

const AUTH_API = '/api';
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
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      }),
      params: new HttpParams().set('email', email)
    };
    return this.http.post<APIResponse>(AUTH_API + '/logoff', null, httpOptions);
  }

  forgotPassword(email: string) {
    return this.http.get<APIResponse>(
      AUTH_API + '/forgotpassword',
      {
        headers: new HttpHeaders({'Content-Type': 'application/json'}),
        params: new HttpParams().set('email', email)
      }
    );
  }

  verifyemail(email: string, token: string) {
    return this.http.get<APIResponse>(
      AUTH_API + '/verify',
      {
        headers: new HttpHeaders({'Content-Type': 'application/json'}),
        params: new HttpParams().set('email', email).set('token', token)
      }
    );
  }

  resetPassword(email: string, oldPassword: string, newPassword: string) {
    var resetPasswordRequest = {
      'email': email,
      'oldPassword': oldPassword,
      'newPassword': newPassword
    }
    return this.http.post<APIResponse>(
      AUTH_API + '/verify', resetPasswordRequest,
      {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
      }
    );
  }
}
