import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SignUpCredentials } from '../../../nav-bar/signup/signupcredentials';

const AUTH_API = 'http://localhost:8383';
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  register(signUpCredentials: object) {
    return this.http.post(
      AUTH_API + "/register",
      signUpCredentials,
      httpOptions
    )
  }

  signin(signUpCredentials: object) {
    return this.http.post(
      AUTH_API + '/login',
      signUpCredentials,
      httpOptions
    )
  }
}
