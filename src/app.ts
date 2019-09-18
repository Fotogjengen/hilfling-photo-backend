import express from "express";
import bodyParser from "body-parser";
var pgp = require("pg-promise")(/* options */);
var db = pgp("postgres://postgres:example@postgres:5432/photodb"); // TODO: Changepassword from example to something else

// Create Express server
const app = express();

//Controllers (route handlers)
import * as testController from "./controllers/TestController";

app.set("port", process.env.PORT || 8080);
// Bodyparser middleware
app.use(bodyParser.json());
app.use(
  bodyParser.urlencoded({
    extended: true
  })
);

//Primary app routes
app.get("/", testController.index);
import { Request, Response } from "express";

app.get("/hei", (req: Request, res: Response) => {
  console.log("hei");
  res.status(200).json({ sindre: "hei" });
});

app.get("/test", (req: Request, res: Response) => {
  console.log("hello");
  db.none("INSERT INTO photo(motive) VALUES ($1)", ["sindre"])
    .then(() => {
      console.log("sucess");
    })
    .catch((error: Error) => {
      console.log(error);
    });
});

export default app;
module.exports = app;
