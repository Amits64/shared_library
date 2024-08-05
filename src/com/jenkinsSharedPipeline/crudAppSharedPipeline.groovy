package com.jenkinsSharedPipeline

class CrudAppPipeline {
    static void performDeployment(String image, String tag) {
        echo "Deploying image ${image}:${tag}"
        // Add deployment logic here
    }
}
