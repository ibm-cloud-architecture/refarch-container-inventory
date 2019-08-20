# Container inventory legacy app

This project illustrates the integration of a legacy application with MQ and Kafka or with a change data capture implementation. The application manages the current container inventory for the shipment company, introduced in the end to end solution [here.](https://ibm-cloud-architecture.github.io/refarch-kc/). 

The JEE application is a 3 tiers architecture, used to manage container inventory. As part of those containers are the Reefer ones. So the approach is to use the legacy application to keep manage the inventory but use MQ or CDC to propagate the inventory updates to the microservice world. 

We recommend readding the content from the [book view.](https://ibm-cloud-architecture.github.io/refarch-container-inventory/)

## Build and Run the JEE app



## Building this booklet locally

The content of this repository is written with markdown files, packaged with [MkDocs](https://www.mkdocs.org/) and can be built into a book-readable format by MkDocs build processes.

1. Install MkDocs locally following the [official documentation instructions](https://www.mkdocs.org/#installation).
2. `git clone https://github.com/ibm-cloud-architecture/refarch-container-inventory.git` _(or your forked repository if you plan to edit)_
3. `cd refarch-container-inventory`
4. `mkdocs serve`
5. Go to `http://127.0.0.1:8000/` in your browser.

### Pushing the book to GitHub Pages

1. Ensure that all your local changes to the `master` branch have been committed and pushed to the remote repository.
   1. `git push origin master`
2. Ensure that you have the latest commits to the `gh-pages` branch, so you can get others' updates.
	```bash
	git checkout gh-pages
	git pull origin gh-pages
	
	git checkout master
	```
3. Run `mkdocs gh-deploy` from the root refarch-kc directory.

--- 

## Contribute

As this implementation solution is part of the Event Driven architeture reference architecture, the [contribution policies](./CONTRIBUTING.md) apply the same way here.

**Contributors:**
* [Jerome Boyer](https://www.linkedin.com/in/jeromeboyer/)
* [Daniel Cogswell](https://www.linkedin.com/in/danutek/)
* [Hemankita Perabathini](https://www.linkedin.com/in/hemankita-perabathini/)

Please [contact me](mailto:boyerje@us.ibm.com) for any questions.