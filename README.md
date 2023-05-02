<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/spbu-coding-2022/trees-12">
    <img src="https://media.discordapp.net/attachments/760917929126133834/1102646230024192051/image.png?width=320&height=320" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Search Trees Project</h3>

  <p align="center">
    AVL, Red-Black and Binary Search Trees models.
    <br />
    <br />
    <a href="https://t.me/kkkebab_boy">Report Bug</a>
    ·
    <a href="https://t.me/ASpectreTG">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#used-technologies">Used Technologies</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#clone-repository">Clone Repository</a></li>
      </ul>
    </li>
    <li><a href="#app-usage">App Usage</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

Many people use search engines to classify various information, as well as quickly obtain the necessary data.
Our task is to study this issue in practice, which implies the development of search trees with a detailed study of all the subtleties of each model.

Types of search trees that we are going to implement:
* A simple Binary Search Tree.
* Red-Black Search Tree.
* AVL Search Tree.

Of course, our task is not only to develop the algorithm of the application itself, but also to implement the user interface to work with it, create test coverage and decent documentation.

<p align="right">(<a href="#search-trees-project">Back to top</a>)</p>



### Used Technologies

Technologies used to develop the project:

* [![gradle](https://img.shields.io/badge/gradle-FFFFFF?style=for-the-badge&logo=gradle&logoColor=black&)](https://gradle.org/)
* [![gradle](https://img.shields.io/badge/kotlin-FFFFFF?style=for-the-badge&logo=kotlin&logoColor=black&)](https://kotlinlang.org/)
* [![gradle](https://img.shields.io/badge/junit-FFFFFF?style=for-the-badge&logo=junit&logoColor=black&)](https://junit.org/)
* [![gradle](https://img.shields.io/badge/neo4j-FFFFFF?style=for-the-badge&logo=neo4j&logoColor=black&)](https://neo4j.com)
* [![gradle](https://img.shields.io/badge/sqlite-FFFFFF?style=for-the-badge&logo=sqlite&logoColor=black&)](https://www.sqlite.org/index.html)
* [![gradle](https://img.shields.io/badge/docker-FFFFFF?style=for-the-badge&logo=docker&logoColor=black&)](https://www.docker.com)
* [![gradle](https://img.shields.io/badge/compose-FFFFFF?style=for-the-badge&logo=compose&logoColor=black&)](https://www.jetbrains.com/ru-ru/lp/compose-multiplatform/)

<p align="right">(<a href="#search-trees-project">Back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

To start working with our development, you need to clone repository:

* git

  ```sh
  git clone https://github.com/spbu-coding-2022/trees-12.git
  ```

To initialize the library and start working with it, you need to know the following lines:

* Initializing BinarySearchTree (default RedBlackTree):

  ```kotlin
  val tree = binarySearchTreeOf<KeyType, ValueType>()
  ```

* Initializing simple BinarySearchTree:

  ```kotlin
  val tree = SimpleBinarySearchTree<KeyType, ValueType>()
  ```

* Initializing RedBlackTree:

  ```kotlin
  val tree = RedBlackTree<KeyType, ValueType>()
  ```

* Initializing AVLTree:

  ```kotlin
  val tree = AVLTree<KeyType, ValueType>()
  ```

To work with trees, you also need to know the management commands:

* Inserting a value by key:

  ```kotlin
  tree[key] = value
  ```

* Getting a value by key:

  ```kotlin
  val value = tree[key]
  ```

* Deleting a value by key:

  ```kotlin
  tree.remove(key)
  ```

<!-- APP USAGE -->
## App Usage

Before launching the application, you need to run "neo4j" via "docker":

* This is done by the command in project repository:

```sh
docker compose up -d
```

You also need to build the application and run it:

* This is done by the command:

```sh
./gradlew run
```

A little bit about the user interface:

* After launching the application, a window will appear in which you can select the desired type of tree, as well as previously saved models of this type.

<img src="https://media.discordapp.net/attachments/760917929126133834/1102882512851701823/image.png?width=1191&height=670" alt="Start" width="400" height="180">

<p align="right">(<a href="#search-trees-project">Back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.

<p align="right">(<a href="#search-trees-project">Back to top</a>)</p>



<!-- CONTACT -->
## Contact

Baitenov Arsene • [Telegram](https://t.me/ASpectreTG) • arsenebaitenov@gmail.com \
Gryaznov Artem • [Telegram](https://t.me/kkkebab_boy) • gryaznovasm@gmail.com

Project Link • [https://github.com/spbu-coding-2022/trees-12](https://github.com/spbu-coding-2022/trees-12)

<p align="right">(<a href="#search-trees-project">Back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

The resources that we used to get information about binary search trees, their features and implementation possibilities:

* [MIT License](https://mit-license.org)
* [Binary Search Tree](https://en.wikipedia.org/wiki/Search_tree)
* [AVL Search Tree](https://en.wikipedia.org/wiki/AVL_tree)
* [Red-Black Search Tree](https://en.wikipedia.org/wiki/Red–black_tree)
* [Gradle Documentation](https://docs.gradle.org/current/userguide/userguide.html)
* [JUnit Documentation](https://junit.org/junit5/docs/current/user-guide/)
* [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)
* [Mergeable Documentation](https://mergeable.readthedocs.io/en/latest/configuration.html#basics)
* [Compose Documentation](https://developer.android.com/jetpack/compose/documentation)
* [Docker Desktop](https://docs.docker.com/desktop/)

<p align="right">(<a href="#search-trees-project">Back to top</a>)</p>