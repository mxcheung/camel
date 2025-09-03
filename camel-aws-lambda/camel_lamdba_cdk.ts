import * as cdk from 'aws-cdk-lib';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as iam from 'aws-cdk-lib/aws-iam';

const cluster = new ecs.Cluster(this, 'Cluster', {
  vpc,
});

const taskDefinition = new ecs.FargateTaskDefinition(this, 'TaskDef', {
  cpu: 512,
  memoryLimitMiB: 1024,
});

// Grant Lambda invoke permission to the ECS task role
taskDefinition.addToTaskRolePolicy(new iam.PolicyStatement({
  effect: iam.Effect.ALLOW,
  actions: ["lambda:InvokeFunction"],
  resources: ["arn:aws:lambda:ap-southeast-2:123456789012:function:processOrder"], // replace with your Lambda ARN
}));

const container = taskDefinition.addContainer('CamelApp', {
  image: ecs.ContainerImage.fromRegistry("my-camel-app:latest"),
  logging: ecs.LogDrivers.awsLogs({ streamPrefix: "camel" }),
});

new ecs.FargateService(this, 'Service', {
  cluster,
  taskDefinition,
  desiredCount: 1,
});
