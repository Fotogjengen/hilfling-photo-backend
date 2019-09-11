import express from "express";
import bodyParser from "body-parser";

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

export default app;
