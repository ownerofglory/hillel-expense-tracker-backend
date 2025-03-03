# K3s Cluster setup
Set up `K3s cluster` (single-node Kubernetes cluster)

##### 1. Install `K3S`  on your server machine

>  Documentation regarding `K3S`: [k3s.io](https://k3s.io/)


Execute following command to install `K3s` on your server and verify the installation
```shell
curl -sfL https://get.k3s.io | sh - 

# Check for Ready node, takes ~30 seconds 
sudo k3s kubectl get node 
```

Kube config file will be located at `/etc/rancher/k3s/config.yaml`.

##### 2. Copy config file into your user's home directory and rename it to `~/.kube/config`
```shell
mkdir ~/.kube
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chmod 644 ~/.kube/config
```

If you don't need `HTTPS` for now **you can skip step 3** and move on to the  [next
section](#deployment)


For `HTTPS` support please proceed to the step 3

##### 3. (Recommended) Set up certificate manager (for HTTPS)
> Reference used: [Easy steps to install K3s with SSL certificate by traefik, cert manager and Let’s Encrypt](https://levelup.gitconnected.com/easy-steps-to-install-k3s-with-ssl-certificate-by-traefik-cert-manager-and-lets-encrypt-d74947fe7a8)

- Install Helm [from here](https://helm.sh/docs/intro/install/)

Or:
```shell
    curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
    chmod 700 get_helm.sh
    ./get_helm.sh
```
- Add helm repo
```shell
helm repo add jetstack https://charts.jetstack.io
helm repo update
```
- Install cert manager via Helm
```shell
helm install \
 cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --set installCRDs=true
```
- Verify certificate manager
```shell
kubectl -n cert-manager get pod
```
![](./assets/cert-man-verify.png)
