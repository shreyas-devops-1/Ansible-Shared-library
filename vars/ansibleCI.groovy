import org.devops.AnsibleHelper

def call(Map config = [:]) {

    def inventory = config.inventory ?: 'inventory.ini'
    def playbook = config.playbook ?: 'playbook.yml'

    def helper = new AnsibleHelper(this)

    pipeline {
        agent any

        environment {
            ANSIBLE_HOST_KEY_CHECKING = 'False'
        }

        stages {

            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('YAML Lint') {
                steps {
                    script {
                        helper.yamlLint()
                    }
                }
            }

            stage('Ansible Lint') {
                steps {
                    script {
                        helper.ansibleLint(playbook)
                    }
                }
            }

            stage('Syntax Check') {
                steps {
                    script {
                        helper.syntaxCheck(inventory, playbook)
                    }
                }
            }

            stage('Dry Run Validation') {
                steps {
                    script {
                        helper.dryRunCheck(inventory, playbook)
                    }
                }
            }
        }

        post {
            success {
                echo 'ScyllaDB Ansible Playbook CI Passed'
            }

            failure {
                echo 'ScyllaDB Ansible Playbook CI Failed'
            }
        }
    }
}
