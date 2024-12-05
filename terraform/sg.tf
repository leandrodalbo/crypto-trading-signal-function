resource "aws_security_group" "lambda-sg" {
  name        = "${var.env}-${var.lambda_function}-sg"
  description = "trading_signals_lambda_sg"
  vpc_id      = data.terraform_remote_state.resources.outputs.vpc_id
}

resource "aws_security_group_rule" "lambdain" {
  type              = "ingress"
  security_group_id = aws_security_group.lambda-sg.id

  from_port   = 0
  to_port     = 0
  protocol    = "-1"
  cidr_blocks = ["0.0.0.0/0"]

}

resource "aws_security_group_rule" "lambdaout" {
  type              = "egress"
  security_group_id = aws_security_group.lambda-sg.id

  from_port   = 0
  to_port     = 0
  protocol    = "-1"
  cidr_blocks = ["0.0.0.0/0"]
}
