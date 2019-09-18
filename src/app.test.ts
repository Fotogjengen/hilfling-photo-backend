import express from "express";
import { Response, Request } from "express";
const request = require("supertest");
const app = require("./app");

describe("Test the root path", () => {
  test("It should response the GET method", done => {
    request(app)
      .get("/")
      .then((response: Response) => {
        expect(response.statusCode).toBe(200);
        done();
      });
  });
});

describe("Test tests", () => {
  it("Should return true", () => {
    expect(true).toBe(true);
  });
});

