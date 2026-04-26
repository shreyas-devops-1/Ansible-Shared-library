package org.devops

class AnsibleHelper implements Serializable {

    def steps

    AnsibleHelper(steps) {
        this.steps = steps
    }

    def yamlLint() {
        steps.sh 'yamllint .'
    }

    def ansibleLint(String playbook) {
        steps.sh "ansible-lint ${playbook}"
    }

    def syntaxCheck(String inventory, String playbook) {
        steps.sh "ansible-playbook -i ${inventory} ${playbook} --syntax-check"
    }

    def dryRunCheck(String inventory, String playbook) {
        steps.sh "ansible-playbook -i ${inventory} ${playbook} --check"
    }
}
