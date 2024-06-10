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
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQURNSU4iLCJlbWFpbCI6ImFkbWluQHlvcG1haWwuY29tIiwic3ViIjoiYWRtaW5AeW9wbWFpbC5jb20iLCJpYXQiOjE3MTc4MzgzNzAsImV4cCI6MTcxNzkyNDc3MH0.OC4D5kTwr22WYG6QVEnkrNUM9nUnmZ6R2XxTu7VWkh6LnhbrCTAYDKE6YyRHYlmElwpttO4mN9lOOPI-fJrYvg'
      }),
      params: new HttpParams().set('email', email)
    };
    var httpParam = new HttpParams().set('email', email);
    return this.http.post<APIResponse>(AUTH_API + '/logoff', null, httpOptions);
  }
}
