# Shared Library

## Overview

The Shared Library provides reusable GitHub Actions workflows and Terraform configurations to streamline and standardize deployment processes. This library includes workflows for deploying infrastructure, managing AWS resources, and integrating with Terraform and Kubernetes.

## Table of Contents

- [Overview](#overview)
- [Setup](#setup)
- [Usage](#usage)
- [Contribution Guidelines](#contribution-guidelines)
- [License](#license)

## Setup

To use the workflows and configurations provided in this library, follow these steps:

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-organization/shared-library.git
   cd shared-library
   ```

2. **Configure GitHub Secrets**

   Ensure you have the necessary secrets configured in your GitHub repository. Required secrets include:

   - `AWS_ACCESS_KEY_ID`
   - `AWS_SECRET_ACCESS_KEY`
   - `TF_CLOUD_TOKEN`
   - `AWS_REGION`

   You can set these secrets in your GitHub repository settings under `Settings` > `Secrets and variables` > `Actions`.

3. **Configure Terraform Cloud Token**

   Add the Terraform Cloud token to your GitHub secrets under the name `TF_CLOUD_TOKEN`.

4. **Directory Structure**

   The repository contains the following key directories and files:

   - `.github/workflows/`: Contains GitHub Actions workflows for deployment and other tasks.
   - `terraform/`: Contains Terraform configurations for infrastructure deployment.

## Usage

### GitHub Actions Workflows

The library includes reusable GitHub Actions workflows. To use a workflow in your repository:

1. **Reference the Workflow**

   In your `.github/workflows` directory, create or update a workflow YAML file to call the shared workflow:

   ```yaml
   name: Example Workflow

   on:
     push:
       branches:
         - main

   jobs:
     example:
       uses: your-organization/shared-library/.github/workflows/example-workflow.yml@main
       with:
         example-input: "value"
   ```

2. **Inputs and Outputs**

   Each workflow may require specific inputs and produce outputs. Refer to the workflow documentation for details on available inputs and outputs.

### Terraform Configurations

To use the Terraform configurations provided by the library:

1. **Initialize Terraform**

   In your Terraform project directory, initialize Terraform:

   ```bash
   terraform init
   ```

2. **Configure Your Terraform Files**

   Reference the modules or configurations provided by the shared library in your Terraform files:

   ```hcl
   module "example" {
     source = "your-organization/shared-library/terraform/example"
     version = "1.0.0"
     # Module inputs
   }
   ```

3. **Apply Terraform Configurations**

   Apply the configurations to provision your resources:

   ```bash
   terraform apply
   ```

## Contribution Guidelines

We welcome contributions to the Shared Library! To contribute:

1. **Fork the Repository**

   Click the "Fork" button on GitHub to create your own copy of the repository.

2. **Create a Branch**

   Create a new branch for your changes:

   ```bash
   git checkout -b feature/new-feature
   ```

3. **Make Your Changes**

   Edit files, add features, or fix issues as needed.

4. **Submit a Pull Request**

   Push your changes to your fork and create a pull request against the `main` branch of the original repository.

5. **Follow the Code of Conduct**

   Ensure that your contributions adhere to our [Code of Conduct](CODE_OF_CONDUCT.md).

## License

This repository is licensed under the [MIT License](LICENSE).

---

For more information, please refer to the documentation provided within the workflows and Terraform modules or contact the repository maintainers.

### Customization Tips
- **Update Repository URLs**: Replace placeholder URLs and repository names with your actual organization’s repository URLs.
- **Add Detailed Descriptions**: If you have specific modules or workflows, include detailed descriptions of their purpose and usage.
- **Add More Sections**: Depending on your library’s features, you might want to include additional sections like `Troubleshooting`, `FAQs`, or `Release Notes`.

---
Feel free to adjust the content according to your specific needs and requirements.