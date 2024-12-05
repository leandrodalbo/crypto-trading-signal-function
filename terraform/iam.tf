resource "aws_iam_role" "lambda_role" {
  name               = "${var.env}-${var.lambda_function}-lambda-rl"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
          "Service": [
            "lambda.amazonaws.com"
          ]
      },
      "Action": "sts:AssumeRole"
    }
  ] 
}
EOF
}

resource "aws_iam_role_policy" "lambda_role_policy" {
  name   = "${var.env}-${var.lambda_function}-pol"
  role   = aws_iam_role.lambda_role.id
  policy = <<EOF
    {
      "Version": "2012-10-17",
      "Statement": [
      {
        "Effect": "Allow",
        "Action": [
          "cloudwatch:*",
          "rds:*",
          "sns:*",
          "ssm:*",
          "logs:*",
          "s3:*",
          "ec2:*"
        ],
        "Resource":"*"
      }
    ] 
  }
  EOF
}