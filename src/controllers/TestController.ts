import { Request, Response } from "express";

export const index = (req: Request, res: Response) => {
  return res.status(200).json({ pernille: "hei" });
};
