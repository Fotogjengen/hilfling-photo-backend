"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const body_parser_1 = __importDefault(require("body-parser"));
// Create Express server
const app = express_1.default();
//Controllers (route handlers)
app.set("port", process.env.PORT || 8080);
// Bodyparser middleware
app.use(body_parser_1.default.json());
app.use(body_parser_1.default.urlencoded({
    extended: true
}));
//Primary app routes
/*TODO
ex:
app.get("/", homeController.index);
*/
exports.default = app;
//# sourceMappingURL=app.js.map